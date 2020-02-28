import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;
import java.security.PublicKey;


public class JwtTest {

    private static final String pubKeyPath = "C:\\Users\\liuha\\Documents\\GitHub\\Project_leyou\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\Users\\liuha\\Documents\\GitHub\\Project_leyou\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU4MjcyMTM3Nn0.GEhYHWo_KQ2H1yWAtRn4yDjHDBhxCt0XGY6fbro5Kr-pivy6_SH56Lxy7X0pkZMLExX7o4HkaI7Eg4mWolcbOufhbL0XzHVAZY0WCME1OzORBZ8GL-6XHHBuHIXjFdvFyWiOEKJbrN93SVFT9bqU7i8mv4jSzBV8Mg5tOSz3mHo";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}