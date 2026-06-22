import request from '@/utils/request'

/**
 * 新增标签
 */
export function addTagApi(data) {
  return request.post('/tag', data)
}

/**
 * 编辑标签
 */
export function updateTagApi(data) {
  return request.put('/tag', data)
}

/**
 * 删除标签
 */
export function deleteTagApi(id) {
  return request.delete(`/tag/${id}`)
}

/**
 * 分页查询标签
 */
export function pageTagsApi(params) {
  return request.get('/tag/page', { params })
}

/**
 * 查询所有标签
 */
export function listAllTagsApi() {
  return request.get('/tag/all')
}
