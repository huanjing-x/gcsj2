import request from '@/utils/request'

/**
 * AI 代码处理（添加注释 / BUG检测 / 重构优化）
 * @param {Object} data - { codeContent, language, actionType }
 *   actionType: 'comment' | 'debug' | 'refactor'
 */
export function aiProcessApi(data) {
  return request.post('/ai/process', data)
}
