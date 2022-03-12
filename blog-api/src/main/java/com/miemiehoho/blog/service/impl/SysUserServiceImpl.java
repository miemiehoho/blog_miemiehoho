package com.miemiehoho.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miemiehoho.blog.dao.mapper.SysUserMapper;
import com.miemiehoho.blog.dao.pojo.SysUser;
import com.miemiehoho.blog.service.LoginService;
import com.miemiehoho.blog.service.SysUserService;
import com.miemiehoho.blog.vo.ErrorCode;
import com.miemiehoho.blog.vo.LoginUserVO;
import com.miemiehoho.blog.vo.Result;
import com.miemiehoho.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author miemiehoho
 * @date 2021/11/17 19:52
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("miemiehuhu");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);//遵循用多少查多少原则
        queryWrapper.last("limit 1");// 查到一条就停止，不需要继续查找，提高效率
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *      是否为空、解析是否成功、redis是否存在
         * 2.如果校验失败返回错误
         * 3.如果成功返回对应结果：LoginUserVo
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            return Result.fail(ErrorCode.TOEKN_ERROR.getCode(), ErrorCode.TOEKN_ERROR.getMsg());
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(sysUser, loginUserVO);
        loginUserVO.setId(String.valueOf(sysUser.getId()));
        return Result.success(loginUserVO);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        // id会自动生成
        // MybatisPlus默认生成分布式id，雪花算法
        sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setNickname("miemiehuhu");
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }
}
