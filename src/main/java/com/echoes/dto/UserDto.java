package com.echoes.dto;

import com.echoes.entity.User;
import lombok.Data;

/**
 * @Author : Xiaozp
 * @ClassName : UserDto
 * @Description : TODO
 * @create : 2022/7/30 13:28
 * @Version : v1.0
 * @Powered By Corner Lab
 */
@Data
public class UserDto extends User {
    private Integer code;
}
