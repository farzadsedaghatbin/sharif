package com.isc.npsd.sharif.util;

import java.util.*;

/**
 * Created by a_jankeh on 1/30/2017.
 */
public class ParticipantUtil {

    private static ParticipantUtil instant = new ParticipantUtil();
    private static Map<String, String> participants;

    private ParticipantUtil() {
        participants = new HashMap<>();
        participants.put("BKMTIRTHXXX", "IR170120000000004260053744");
        participants.put("BKMNIRTHXXX", "IR750140059965441400000001");
        participants.put("SABCIRTHXXX", "IR170560089935000002001001");
        participants.put("BTOSIRTHXXX", "IR900510019509200700001001");
        participants.put("BEGNIRTHXXX", "IR810550019934100010001002");
        participants.put("AYBKIRTHXXX", "IR080620000000300043490007");
        participants.put("TOSMIRTHXXX", "IR740640002100710010000002");
        participants.put("KESHIRTHXXX", "IR040160000000000000049717");
        participants.put("MEHRIRTHXXX", "IR610600309071000069883001");
        participants.put("BTEJIRTHXXX", "IR750180000000000004803248");
        participants.put("SEPBIRTHXXX", "IR850150000001426327112407");
        participants.put("BKBPIRTHXXX", "IR300570011776510000002102");
        participants.put("REFAIRTHXXX", "IR630130100000009999002183");
        participants.put("SINAIRTHXXX", "IR980590010230001000000002");
        participants.put("TTBIIRTHXXX", "IR480220100102110010000002");
        participants.put("MELIIRTHXXX", "IR670170000000108643339000");
        participants.put("DAYBIRTHXXX", "IR050660000000100107636006");
        participants.put("PBIRIRTHXXX", "IR180210041420129085868701");
        participants.put("SRMBIRTHXXX", "IR800580017234500000001001");
        participants.put("IRZAIRTHXXX", "IR040690019881000000001001");
        participants.put("KBIDIRTHXXX", "IR750530000001900223959605");
        participants.put("BKPAIRTHXXX", "IR250540100202100016252003");
        participants.put("BOIMIRTHXXX", "IR850110000000100018195002");
        participants.put("BSIRIRTHXXX", "IR560190000000216155519000");
        participants.put("EDBIIRTHXXX", "IR820200000000216155519000");
        participants.put("ANSBIRTHXXX", "IR740630251180500463986001");
        participants.put("CIYBIRTHXXX", "IR190610000007001674614137");
        participants.put("HEKMIRTHXXX", "IR700650220170001209645001");
        participants.put("GHVMIRTHXXX", "IR240520000014410500051257");
        participants.put("RESAIRTHXXX", "IR080700037000111610147001");
    }


    public List<String> getBics() {
        List<String> bics = new ArrayList<>();
        Set<String> keys = participants.keySet();
        for (String key : keys) {
            bics.add(key);
        }
        return bics;
    }

    public static ParticipantUtil getInstance() {
        if (instant != null)
            return instant;
        else
            return new ParticipantUtil();
    }

    private int getSize() {
        return getBics().size();
    }

    public String getRandomParticipant() {
        int bound = getSize();
        Random random = new Random();
        int indx = random.nextInt(bound);
        return getBics().get(indx);
    }

    public String getIBAN(String bic) {
        return participants.get(bic);
    }
}
