package springboot.service;

import org.jackdking.core.bean.User;
import org.jackdking.core.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jackdking.multids.annotation.DBType;
import springboot.MySolution_dynamicdatasource.proxyautowired.SelfBeanProxyAware;

@Service
public class UserService implements SelfBeanProxyAware{

    @Autowired
    private UserMapper userMapper;
    
    private UserService userServiceProxy;

    public void select(){
        User user = userMapper.selectByPrimaryKey(11);
        System.out.println(user.getId() + "--" + user.getName() + "==" + user.getGender());
    }

    @DBType(value="ds1")
    public void select2() {
    	User user = userMapper.selectByPrimaryKey(11);
        System.out.println("SLAVE:"+user.getId() + "--" + user.getName() + "==" + user.getGender());
    }
    

    @DBType(value="ds1")
    public void select1() {
        User user = userMapper.selectByPrimaryKey(11);
        System.out.println("MASTER:"+user.getId() + "--" + user.getName() + "==" + user.getGender());
        this.select2();
    } 
    @DBType(value="ds1")
    public void select3() {
        User user = userMapper.selectByPrimaryKey(11);
        System.out.println("MASTER:"+user.getId() + "--" + user.getName() + "==" + user.getGender());
        userServiceProxy.select2();
    } 
 
    

    @DBType(value="ds1")
    public void insert2Master(User user){
        userMapper.insertSelective(user);
    }
    
    @DBType(value="ds2")
    public void insert2Slave(User user){
        userMapper.insertSelective(user);
    }
  

	@Override
	public void setSelBeanfProxy(Object proxyObj) {
		// TODO Auto-generated method stub
		this.userServiceProxy = (UserService)proxyObj;
	}
}