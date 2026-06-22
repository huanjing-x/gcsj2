package com.gcsj2.security;

/**
 * 用户上下文 - 使用 ThreadLocal 存储当前请求的用户信息
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前用户
     */
    public static void setCurrentUser(Long userId, String username) {
        USER_ID_HOLDER.set(userId);
        USERNAME_HOLDER.set(username);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        return USERNAME_HOLDER.get();
    }

    /**
     * 清除用户信息（请求结束后调用，防止内存泄漏）
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
        USERNAME_HOLDER.remove();
    }
}
