package io.metersphere.system.service;

import io.metersphere.system.domain.UserInvite;
import io.metersphere.system.dto.request.UserRegisterRequest;
import io.metersphere.system.dto.user.request.UserBatchCreateRequest;

/**
 * 系统用户相关接口
 */
public interface UserXpackService {

    int guessWhatHowToAddUser(UserBatchCreateRequest userCreateDTO, String source, String operator);

    int guessWhatHowToAddUser(UserRegisterRequest registerRequest, UserInvite userInvite) throws Exception;
}
