import { http } from './index'

/**
 * 用户个人资料响应类型
 */
export interface UserProfileResponse {
  userId: number
  username: string
  nickname: string
  avatarUrl: string
  bio: string
  followingCount: number
  followerCount: number
  likesReceived: number
}

/**
 * 获取当前登录用户的个人资料
 */
export const getProfile = () => {
  return http.get<UserProfileResponse>('/api/user/profile')
}
