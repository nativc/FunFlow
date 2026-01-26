import { http } from './index'

/**
 * 创建视频
 */
export interface CreateVideoRequest {
  video: File
  cover?: File
  title?: string
  tags: string[]
  isPublic: number
}

/**
 * 创建视频作品
 */
export const createVideo = (data: CreateVideoRequest): Promise<any> => {
  const formData = new FormData()
  
  formData.append('video', data.video)
  if (data.cover) {
    formData.append('cover', data.cover)
  }
  if (data.title) {
    formData.append('title', data.title)
  }
  formData.append('tags', JSON.stringify(data.tags))
  formData.append('isPublic', data.isPublic.toString())
  
  return http.post('/api/videos', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
