package ru.shurikvo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tagger {
    private final ByteMatter byt = new ByteMatter();
    private HashMap<String,String> hmTags = new HashMap<>();

    public String parse(byte[] bTag) {
        if(!hmTags.containsKey(byt.toHexString(bTag)))
            return "Неизвестный тег";
        return hmTags.get(byt.toHexString(bTag));
    }

    public List<String> parseAFL(byte[] bAFL) {
        int nOff = 0;
        List<String> sfList = new ArrayList<>();

        while(nOff < bAFL.length) {
            byte[] bL = new byte[2];
            bL[1] = (byte)(bAFL[nOff] | 0x04);
            bL[0] = bAFL[nOff+1];
            sfList.add(byt.toHexString(bL));
            while(bL[0] < bAFL[nOff+2]) {
                bL[0]++;
                sfList.add(byt.toHexString(bL));
            }
            nOff += 4;
        }
        return sfList;
    }

    public Tagger() {
        if(!hmTags.containsKey("5F57")) hmTags.put("5F57","AccountType");
        if(!hmTags.containsKey("9F01")) hmTags.put("9F01","AcquirerIdentifier");
        if(!hmTags.containsKey("9F40")) hmTags.put("9F40","AdditionalTerminalCapabilities");
        if(!hmTags.containsKey("81")) hmTags.put("81","AmountAuthorisedBinary");
        if(!hmTags.containsKey("9F02")) hmTags.put("9F02","AmountAuthorisedNumeric");
        if(!hmTags.containsKey("9F04")) hmTags.put("9F04","AmountOtherBinary");
        if(!hmTags.containsKey("9F03")) hmTags.put("9F03","AmountOtherNumeric");
        if(!hmTags.containsKey("9F3A")) hmTags.put("9F3A","AmountReferenceCurrency");
        if(!hmTags.containsKey("9F26")) hmTags.put("9F26","ApplicationCryptogram");
        if(!hmTags.containsKey("9F42")) hmTags.put("9F42","ApplicationCurrencyCode");
        if(!hmTags.containsKey("9F44")) hmTags.put("9F44","ApplicationCurrencyExponent");
        if(!hmTags.containsKey("9F05")) hmTags.put("9F05","ApplicationDiscretionaryData");
        if(!hmTags.containsKey("5F25")) hmTags.put("5F25","ApplicationEffectiveDate");
        if(!hmTags.containsKey("5F24")) hmTags.put("5F24","ApplicationExpirationDate");
        if(!hmTags.containsKey("94")) hmTags.put("94","AFL");
        if(!hmTags.containsKey("4F")) hmTags.put("4F","ApplicationDedicatedFileName");
        if(!hmTags.containsKey("9F06")) hmTags.put("9F06","ApplicationIdentifierTerminal");
        if(!hmTags.containsKey("82")) hmTags.put("82","AIP");
        if(!hmTags.containsKey("50")) hmTags.put("50","ApplicationLabel");
        if(!hmTags.containsKey("9F12")) hmTags.put("9F12","ApplicationPreferredName");
        if(!hmTags.containsKey("5A")) hmTags.put("5A","PAN");
        if(!hmTags.containsKey("5F34")) hmTags.put("5F34","PAN_SN");
        if(!hmTags.containsKey("87")) hmTags.put("87","ApplicationPriorityIndicator");
        if(!hmTags.containsKey("9F3B")) hmTags.put("9F3B","ApplicationReferenceCurrency");
        if(!hmTags.containsKey("9F43")) hmTags.put("9F43","ApplicationReferenceCurrencyExponent");
        if(!hmTags.containsKey("61")) hmTags.put("61","ApplicationTemplate");
        if(!hmTags.containsKey("9F36")) hmTags.put("9F36","ATC");
        if(!hmTags.containsKey("9F07")) hmTags.put("9F07","ApplicationUsageControl");
        if(!hmTags.containsKey("9F08")) hmTags.put("9F08","ApplicationVersionNumberICC");
        if(!hmTags.containsKey("9F09")) hmTags.put("9F09","ApplicationVersionNumberTerminal");
        if(!hmTags.containsKey("89")) hmTags.put("89","AuthorisationCode");
        if(!hmTags.containsKey("8A")) hmTags.put("8A","AuthorisationResponseCode");
        if(!hmTags.containsKey("5F54")) hmTags.put("5F54","BankIdentifierCode");
        if(!hmTags.containsKey("8C")) hmTags.put("8C","CDOL1");
        if(!hmTags.containsKey("8D")) hmTags.put("8D","CDOL2");
        if(!hmTags.containsKey("5F20")) hmTags.put("5F20","CardholderName");
        if(!hmTags.containsKey("9F0B")) hmTags.put("9F0B","CardholderNameExtended");
        if(!hmTags.containsKey("8E")) hmTags.put("8E","CardholderVerificationMethodList");
        if(!hmTags.containsKey("9F34")) hmTags.put("9F34","CVM_Results");
        if(!hmTags.containsKey("8F")) hmTags.put("8F","CertificationAuthorityPublicKeyIndexICC");
        if(!hmTags.containsKey("9F22")) hmTags.put("9F22","CertificationAuthorityPublicKeyIndexTerminal");
        if(!hmTags.containsKey("83")) hmTags.put("83","CommandTemplate");
        if(!hmTags.containsKey("9F27")) hmTags.put("9F27","CryptogramInformationData");
        if(!hmTags.containsKey("9F45")) hmTags.put("9F45","DAC");
        if(!hmTags.containsKey("84")) hmTags.put("84","DedicatedFileName");
        if(!hmTags.containsKey("9D")) hmTags.put("9D","DirectoryDefinitionFileName");
        if(!hmTags.containsKey("73")) hmTags.put("73","DirectoryDiscretionaryTemplate");
        if(!hmTags.containsKey("9F49")) hmTags.put("9F49","DDOL");
        if(!hmTags.containsKey("BF0C")) hmTags.put("BF0C","FileControlInformationIssuerDiscretionaryData");
        if(!hmTags.containsKey("A5")) hmTags.put("A5","FileControlInformationProprietaryTemplate");
        if(!hmTags.containsKey("6F")) hmTags.put("6F","FileControlInformationTemplate");
        if(!hmTags.containsKey("9F4C")) hmTags.put("9F4C","ICC_DynamicNumber");
        if(!hmTags.containsKey("9F2D")) hmTags.put("9F2D","ICC_PINEnciphermentPublicKeyCertificate");
        if(!hmTags.containsKey("9F2E")) hmTags.put("9F2E","ICC_PINEnciphermentPublicKeyExponent");
        if(!hmTags.containsKey("9F2F")) hmTags.put("9F2F","ICC_PINEnciphermentPublicKeyRemainder");
        if(!hmTags.containsKey("9F46")) hmTags.put("9F46","ICC_PublicKeyCertificate");
        if(!hmTags.containsKey("9F47")) hmTags.put("9F47","ICC_PublicKeyExponent");
        if(!hmTags.containsKey("9F48")) hmTags.put("9F48","ICC_PublicKeyRemainder");
        if(!hmTags.containsKey("9F1E")) hmTags.put("9F1E","InterfaceDeviceSerialNumber");
        if(!hmTags.containsKey("5F53")) hmTags.put("5F53","InternationalBankAccountNumber");
        if(!hmTags.containsKey("9F0D")) hmTags.put("9F0D","IssuerActionCodeDefault");
        if(!hmTags.containsKey("9F0E")) hmTags.put("9F0E","IssuerActionCodeDenial");
        if(!hmTags.containsKey("9F0F")) hmTags.put("9F0F","IssuerActionCodeOnline");
        if(!hmTags.containsKey("9F10")) hmTags.put("9F10","IssuerApplicationData");
        if(!hmTags.containsKey("91")) hmTags.put("91","IssuerAuthenticationData");
        if(!hmTags.containsKey("9F11")) hmTags.put("9F11","IssuerCodeTableIndex");
        if(!hmTags.containsKey("5F28")) hmTags.put("5F28","IssuerCountryCode");
        if(!hmTags.containsKey("5F55")) hmTags.put("5F55","IssuerCountryCodeAlpha2Format");
        if(!hmTags.containsKey("5F56")) hmTags.put("5F56","IssuerCountryCodeAlpha3Format");
        if(!hmTags.containsKey("42")) hmTags.put("42","IssuerIdentificationNumber");
        if(!hmTags.containsKey("90")) hmTags.put("90","IssuerPublicKeyCertificate");
        if(!hmTags.containsKey("9F32")) hmTags.put("9F32","IssuerPublicKeyExponent");
        if(!hmTags.containsKey("92")) hmTags.put("92","IssuerPublicKeyRemainder");
        if(!hmTags.containsKey("86")) hmTags.put("86","IssuerScriptCommand");
        if(!hmTags.containsKey("9F18")) hmTags.put("9F18","IssuerScriptIdentifier");
        if(!hmTags.containsKey("71")) hmTags.put("71","IssuerScriptTemplate1");
        if(!hmTags.containsKey("72")) hmTags.put("72","IssuerScriptTemplate2");
        if(!hmTags.containsKey("5F50")) hmTags.put("5F50","IssuerURL");
        if(!hmTags.containsKey("5F2D")) hmTags.put("5F2D","LanguagePreference");
        if(!hmTags.containsKey("9F13")) hmTags.put("9F13","LastOnlineApplicationTransactionCounterRegister");
        if(!hmTags.containsKey("9F4D")) hmTags.put("9F4D","TransactionLogEntry");
        if(!hmTags.containsKey("9F4F")) hmTags.put("9F4F","LogFormat");
        if(!hmTags.containsKey("9F14")) hmTags.put("9F14","LowerConsecutiveOfflineLimit");
        if(!hmTags.containsKey("9F15")) hmTags.put("9F15","MerchantCategoryCode");
        if(!hmTags.containsKey("9F16")) hmTags.put("9F16","MerchantIdentifier");
        if(!hmTags.containsKey("9F4E")) hmTags.put("9F4E","MerchantNameAndLocation");
        if(!hmTags.containsKey("9F17")) hmTags.put("9F17","PersonalIdentificationNumberTryCounter");
        if(!hmTags.containsKey("9F39")) hmTags.put("9F39","PointOfServiceEntryMode");
        if(!hmTags.containsKey("9F38")) hmTags.put("9F38","PDOL");
        if(!hmTags.containsKey("70")) hmTags.put("70","ReadRecordResponseMessageTemplate");
        if(!hmTags.containsKey("80")) hmTags.put("80","ResponseMessageTemplateFormat1");
        if(!hmTags.containsKey("77")) hmTags.put("77","ResponseMessageTemplateFormat2");
        if(!hmTags.containsKey("5F30")) hmTags.put("5F30","ServiceCode");
        if(!hmTags.containsKey("88")) hmTags.put("88","ShortFileIdentifier");
        if(!hmTags.containsKey("9F4B")) hmTags.put("9F4B","SignedDynamicApplicationData");
        if(!hmTags.containsKey("93")) hmTags.put("93","SignedStaticApplicationData");
        if(!hmTags.containsKey("9F4A")) hmTags.put("9F4A","StaticDataAuthenticationTagList");
        if(!hmTags.containsKey("9F33")) hmTags.put("9F33","TerminalCapabilities");
        if(!hmTags.containsKey("9F1A")) hmTags.put("9F1A","TerminalCountryCode");
        if(!hmTags.containsKey("9F1B")) hmTags.put("9F1B","TerminalFloorLimit");
        if(!hmTags.containsKey("9F1C")) hmTags.put("9F1C","TerminalIdentification");
        if(!hmTags.containsKey("9F1D")) hmTags.put("9F1D","TerminalRiskManagementData");
        if(!hmTags.containsKey("9F35")) hmTags.put("9F35","TerminalType");
        if(!hmTags.containsKey("95")) hmTags.put("95","TerminalVerificationResults");
        if(!hmTags.containsKey("9F1F")) hmTags.put("9F1F","Track1DiscretionaryData");
        if(!hmTags.containsKey("9F20")) hmTags.put("9F20","Track2DiscretionaryData");
        if(!hmTags.containsKey("57")) hmTags.put("57","Track2EquivalentData");
        if(!hmTags.containsKey("97")) hmTags.put("97","TransactionCertificateDataObjectList");
        if(!hmTags.containsKey("98")) hmTags.put("98","TransactionCertificateHashValue");
        if(!hmTags.containsKey("5F2A")) hmTags.put("5F2A","TransactionCurrencyCode");
        if(!hmTags.containsKey("5F36")) hmTags.put("5F36","TransactionCurrencyExponent");
        if(!hmTags.containsKey("9A")) hmTags.put("9A","TransactionDate");
        if(!hmTags.containsKey("99")) hmTags.put("99","TransactionPersonalIdentificationNumberData");
        if(!hmTags.containsKey("9F3C")) hmTags.put("9F3C","TransactionReferenceCurrencyCode");
        if(!hmTags.containsKey("9F3D")) hmTags.put("9F3D","TransactionReferenceCurrencyExponent");
        if(!hmTags.containsKey("9F41")) hmTags.put("9F41","TransactionSequenceCounter");
        if(!hmTags.containsKey("9B")) hmTags.put("9B","TransactionStatusInformation");
        if(!hmTags.containsKey("9F21")) hmTags.put("9F21","TransactionTime");
        if(!hmTags.containsKey("9C")) hmTags.put("9C","TransactionType");
        if(!hmTags.containsKey("9F37")) hmTags.put("9F37","UnpredictableNumber");
        if(!hmTags.containsKey("9F23")) hmTags.put("9F23","UpperConsecutiveOfflineLimit");
        if(!hmTags.containsKey("D3")) hmTags.put("D3","AdditionalCheckTable");
        if(!hmTags.containsKey("D5")) hmTags.put("D5","ApplicationControl");
        if(!hmTags.containsKey("9F7E")) hmTags.put("9F7E","ApplicationLifeCycleData");
        if(!hmTags.containsKey("C3")) hmTags.put("C3","Card_IAC_Decline");
        if(!hmTags.containsKey("C4")) hmTags.put("C4","Card_IAC_Default");
        if(!hmTags.containsKey("C5")) hmTags.put("C5","Card_IAC_Online");
        if(!hmTags.containsKey("8C")) hmTags.put("8C","CDOL1");
        if(!hmTags.containsKey("C7")) hmTags.put("C7","CDOL1_DataLen");
        if(!hmTags.containsKey("8D")) hmTags.put("8D","CDOL2");
        if(!hmTags.containsKey("C6")) hmTags.put("C6","Counters");
        if(!hmTags.containsKey("C8")) hmTags.put("C8","CRMCountryCode");
        if(!hmTags.containsKey("C9")) hmTags.put("C9","CRMCurrencyCode");
        if(!hmTags.containsKey("D1")) hmTags.put("D1","CurrencyConversionTable");
        if(!hmTags.containsKey("9F52")) hmTags.put("9F52","CVR");
        if(!hmTags.containsKey("9F49")) hmTags.put("9F49","DDOL");
        if(!hmTags.containsKey("D6")) hmTags.put("D6","DefaultARPC_ResponseCode");
        if(!hmTags.containsKey("9F4C")) hmTags.put("9F4C","ICC_DynamicNumber");
        if(!hmTags.containsKey("9F10")) hmTags.put("9F10","IssuerApplicationData");
        if(!hmTags.containsKey("91")) hmTags.put("91","IssuerAuthenticationData");
        if(!hmTags.containsKey("9F4F")) hmTags.put("9F4F","LogFormat");
        if(!hmTags.containsKey("9F14")) hmTags.put("9F14","LowerConsecutiveOfflineLimit");
        if(!hmTags.containsKey("CA")) hmTags.put("CA","LowerCOTA");
        if(!hmTags.containsKey("9F50")) hmTags.put("9F50","OfflineBalance");
        if(!hmTags.containsKey("9F17")) hmTags.put("9F17","PIN_TryCounter");
        if(!hmTags.containsKey("DF01")) hmTags.put("DF01","SecurityLimits");
        if(!hmTags.containsKey("DF02")) hmTags.put("DF02","SecurityLimitsStatus");
        if(!hmTags.containsKey("9F23")) hmTags.put("9F23","UpperConsecutiveOfflineLimit");
        if(!hmTags.containsKey("CB")) hmTags.put("CB","UpperCOTA");
    }
}
