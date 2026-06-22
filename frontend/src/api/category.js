import request from '@/utils/request'

/**
 * 新增分类
 */
export function addCategoryApi(data) {
  return request.post('/category', data)
}

/**
 * 编辑分类
 */
export function updateCategoryApi(data) {
  return request.put('/category', data)
}

/**
 * 删除分类
 */
export function deleteCategoryApi(id) {
  return request.delete(`/category/${id}`)
}

/**
 * 分页查询分类
 */
export function pageCategoriesApi(params) {
  return request.get('/category/page', { params })
}

/**
 * 查询所有分类（用于下拉框）
 */
export function listAllCategoriesApi() {
  return request.get('/category/all')
}
