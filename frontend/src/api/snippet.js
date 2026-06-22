import request from '@/utils/request'

/**
 * 新增代码片段
 */
export function addSnippetApi(data) {
  return request.post('/snippet', data)
}

/**
 * 编辑代码片段
 */
export function updateSnippetApi(data) {
  return request.put('/snippet', data)
}

/**
 * 删除代码片段
 */
export function deleteSnippetApi(id) {
  return request.delete(`/snippet/${id}`)
}

/**
 * 分页查询代码片段
 */
export function pageSnippetsApi(params) {
  return request.get('/snippet/page', { params })
}

/**
 * 获取代码片段详情
 */
export function getSnippetDetailApi(id) {
  return request.get(`/snippet/${id}`)
}

/**
 * 切换收藏状态
 */
export function toggleCollectApi(id) {
  return request.put(`/snippet/${id}/collect`)
}

/**
 * 获取代码内容（用于复制）
 */
export function getSnippetContentApi(id) {
  return request.get(`/snippet/${id}/content`)
}
