package Endpoints;

import java.util.*;

import lib.JSON;
import lib.SparkDB;

public class Table {
    /**
     * Search for a document using the document code
     * Request :GET <br>
     * Response : {
     * "0": "{"DocName":"A" , "Verifier":"B", "Writer":"C"}" ,
     * "1" : "{"DocName":"A" , "Verifier":"B", "Writer":"C"}",
     * ...
     * }
     * 
     * @param Key  Encryption Key
     * @param BODY Request Body
     */
    public String run(byte[] BODY, Map<String, String> SESSION_IDS, SparkDB docs) throws Exception {
        String res = "error";

        HashMap<String, String> in = JSON.QHM(new String(BODY));
        if (SESSION_IDS.containsKey(in.get("session_id"))) {
            HashMap<String, String> MainTable = new HashMap<>(); // K=0,1,2 V={"DocName":"A" , "Verifier":"B", "Writer":"C"}
            for(int i = 0;i < docs.num_queries;i++) {
                HashMap<String, String> ROW = docs.get(i);
                MainTable.put(String.valueOf(i), JSON.HMQ( new HashMap<>() {{
                    put("DocName", ROW.get("doc_name"));
                    put("Verifier", ROW.get("verifier"));
                    put("Writer", ROW.get("writer"));
                }}));
            }
            res = JSON.HMQ(MainTable);
        } else {
            res = JSON.HMQ(new HashMap<String, String>() {
                {
                    put("status", "failed");
                    put("msg", "Session ID isn't correct");
                }
            });
        }

        return res;
    }
}
