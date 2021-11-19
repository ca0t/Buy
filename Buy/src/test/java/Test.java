import com.ms.common.HttpClientHelp;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        HttpClientHelp c=new HttpClientHelp();
        Map<String,String> map=new HashMap<>();
        map.put("ProjectName","pjName");
        map.put("Type","type");
        map.put("panelNo","fioadsf");
        c.post("http://192.168.1.20:5000/api/file/test",null,map);
    }
}
