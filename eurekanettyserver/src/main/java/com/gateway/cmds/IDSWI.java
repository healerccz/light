package com.gateway.cmds;

public class IDSWI {
        public String id;
        public String SWI;
        public static IDSWI on(String id){
                IDSWI obj = new IDSWI();
                obj.SWI = "ON";
                obj.id = id;
                return obj;
        }

        public static IDSWI off(String id){
                IDSWI obj = new IDSWI();
                obj.id = id;
                obj.SWI = "OFF";
                return obj;
        }
}
