package cn.itcast.service.client;

import cn.itcast.service.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public User querById(Integer id) {
        User user = new User();
        user.setUserName("服务器正忙");
        return user;
    }
}
