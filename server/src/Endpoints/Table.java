package Endpoints;

import java.util.*;

import lib.JSON;
import lib.SparkDB;

public class Table {
    public String run(String cookies, Map<String, String> SESSION_IDS, String ENC) throws Exception {
        String res = "error";
        String session_id = cookies.split("session_id=")[1];
        if (SESSION_IDS.containsKey(session_id)) {
            SparkDB T = new SparkDB();
            T.readFromFile("./conf/Table.db", ENC);
            T.delete(new HashMap<String, String>() {{
                put("DocNum", "0");
            }});
            String out = "{";
            for(int i = 0;i < T.num_queries;i++) {
                out += "\""+i+"\":"+ JSON.HMQ(T.get(i));
                if(i+1 < T.num_queries) out+=",";
            }
            out+="}";
            out = out.replaceAll("DocNum", "Document number").replaceAll("DocName", "Document name");
            res = out;
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
