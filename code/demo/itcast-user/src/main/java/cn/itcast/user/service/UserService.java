package cn.itcast.user.service;

import cn.itcast.user.mapper.UserMapper;
import cn.itcast.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;//这里会误报错。Mapper使用mybatis的注解，spring不认识。实际上已经注入

    public User querUserById(Integer id){
        return this.userMapper.selectByPrimaryKey(id);
    }

    public List<User> queryUserAll(){
        return this.userMapper.selectAll();
    }

    @Transactional
    public void delectUserById(Integer id){
         this.userMapper.deleteByPrimaryKey(id);
    }
}
