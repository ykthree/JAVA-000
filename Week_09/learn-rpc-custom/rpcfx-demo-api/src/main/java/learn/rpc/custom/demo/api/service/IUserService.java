package learn.rpc.custom.demo.api.service;

import learn.rpc.custom.demo.api.domain.User;

public interface IUserService {

    User findById(int id);

}
