package ru.shurikvo.mifplus;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import ru.shurikvo.apdu.ApduMaster;
import ru.shurikvo.utils.ByteMatter;

public class ScrollingActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private final static String TAG = "nfc_test";

    private String messageInfo;
    private ByteMatter byt = new ByteMatter();

    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = "NFC Info "+android.text.format.DateFormat.format("yyMMddHHmmss", new java.util.Date()).toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "shurikvo@gmail.com" });
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, messageInfo);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        FloatingActionButton fin = (FloatingActionButton) findViewById(R.id.fin);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage(R.string.about, R.string.about_text);
            }
        });

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }
        pendingIntent = PendingIntent.getActivity(this,0,
                new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {

        super.onResume();
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        messageInfo = "========== New Intent:\n";
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            messageInfo += "---------- Tag:\n" + tag.toString();
            byte[] payload = detectTagData(tag).getBytes();
            showInfo();
        }
    }

    private String detectTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        messageInfo += "\n---------- Tag Data:";
        sb.append("UID (hex): ").append(toHex(id).toUpperCase().replace(" ","")).append('\n');
        sb.append("UID (reversed hex): ").append(toReversedHex(id).toUpperCase().replace(" ","")).append('\n');
        sb.append("UID (dec): ").append(toDec(id)).append('\n');
        sb.append("UID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            sb.append("\n[" + tech + "]");
            if (tech.equals(NfcA.class.getName())) {
                sb.append('\n');
                NfcA nfcATag = NfcA.get(tag);

                byte[] bAtqa = nfcATag.getAtqa();
                sb.append("ATQA: ").append(toHex(bAtqa).toUpperCase()).append('\n');

                short nSak = nfcATag.getSak();
                sb.append(String.format("SAK: %02X", nSak)).append('\n');

                /*try {
                    nfcATag.setTimeout(5000);
                    nfcATag.connect();

                    byte[] bResp,
                            bAPDU = {(byte)0xA8,(byte)0x00,(byte)0x90,(byte)0x00,(byte)0x11,(byte)0x22,(byte)0x33,(byte)0x44,(byte)0x55,(byte)0x66,(byte)0x77,(byte)0x88,(byte)0x99,(byte)0xAA,(byte)0xBB,(byte)0xCC,(byte)0xDD,(byte)0xEE,(byte)0xFF};
                    sb.append(">> ").append(byt.toHexString(bAPDU)).append('\n');
                    bResp = nfcATag.transceive(bAPDU);
                    sb.append("<< ").append(byt.toHexString(bResp)).append('\n');

                    nfcATag.close();
                } catch (IOException ex) {
                    sb.append('\n').append(ex.getMessage());
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }*/
            }

            if (tech.equals(IsoDep.class.getName())) {
                int RC;
                String sCmd, sResp;
                sb.append('\n');

                ApduMaster apdu = new ApduMaster();

                RC = apdu.connect(IsoDep.get(tag));
                if(RC < 0) {
                    sb.append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }

                sCmd = "A80090FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
                sResp = apdu.sendApdu(sCmd);
                if(apdu.isError) {
                    sb.append("Ошибка APDU: ").append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }
                sb.append(apdu.message).append('\n');
                sCmd = "A80190FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
                sResp = apdu.sendApdu(sCmd);
                if(apdu.isError) {
                    sb.append("Ошибка APDU: ").append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }
                sb.append(apdu.message).append('\n');
                sCmd = "A80290FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
                sResp = apdu.sendApdu(sCmd);
                if(apdu.isError) {
                    sb.append("Ошибка APDU: ").append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }
                sb.append(apdu.message).append('\n');
                sCmd = "A80390FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
                sResp = apdu.sendApdu(sCmd);
                if(apdu.isError) {
                    sb.append("Ошибка APDU: ").append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }
                sb.append(apdu.message).append('\n');
                sCmd = "A80490FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
                sResp = apdu.sendApdu(sCmd);
                if(apdu.isError) {
                    sb.append("Ошибка APDU: ").append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }
                sb.append(apdu.message).append('\n');
                sCmd = "AA";
                sResp = apdu.sendApdu(sCmd);
                if(apdu.isError) {
                    sb.append("Ошибка APDU: ").append(apdu.message).append('\n');
                    messageInfo += "\n" + sb.toString();
                    return sb.toString();
                }
                sb.append(apdu.message).append('\n');

                apdu.close();;

               /*PSLister pli = new PSLister();
                sb.append(pli.getCardInfo(IsoDep.get(tag)));
                sb.append(pli.getPSList(IsoDep.get(tag)));*/
            }
        }
        //Log.v("test",sb.toString());
        messageInfo += "\n" + sb.toString();
        return sb.toString();
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    //----------------------------------------------------------------------------------------------
    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    public void showInfo() {
        TextView messageText = (TextView) findViewById(R.id.messageText);
        messageText.setText(messageInfo);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showMessage(R.string.app_name , R.string.action_settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}