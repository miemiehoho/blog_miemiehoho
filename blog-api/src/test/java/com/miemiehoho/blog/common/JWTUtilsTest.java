package com.miemiehoho.blog.common;

import com.miemiehoho.blog.utils.JWTUtils;

/**
 * @author miemiehoho
 * @date 2021/11/18 10:36
 */
class JWTUtilsTest {

    public static void main(String[] args) {

        String token = JWTUtils.createToken(1L);
        // eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDc1NDYxNTAsInVzZXJJZCI6MSwiaWF0IjoxNjQ2NjU3MTE3fQ.6pq6y5KBp3vn6oBMr03MU8ZFUhSPPegREEc9Wo4VvzQ
        // eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDc1NDYxNjcsInVzZXJJZCI6MSwiaWF0IjoxNjQ2NjU3MTM1fQ.hhTaEFeUyxmqVHf7g0Ghwtc1-4orqMwl2n99ddiU3Bw
        System.out.println(token);
    }


}