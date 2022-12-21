package Endpoints;

import java.util.*;

import lib.JSON;
import lib.SparkDB;
import lib.log;

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
    public String run(String cookies, Map<String, String> SESSION_IDS, String ENC) throws Exception {
        String res = "error";
        String session_id = cookies.split("session_id=")[1];
        if (SESSION_IDS.containsKey(session_id)) {
            HashMap<String, String> MainTable = new HashMap<>(); // K=0,1,2 V={"DocName":"A" , "Verifier":"B", "Writer":"C"}
            SparkDB T = new SparkDB();
            T.readFromFile("./conf/Table.db", ENC);
            for(int i = 0;i < T.num_queries;i++) {
                MainTable.put(String.valueOf(i), JSON.HMQ(T.get(i)));
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
