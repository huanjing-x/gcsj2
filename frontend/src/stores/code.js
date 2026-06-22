import { defineStore } from 'pinia'
import { ref } from 'vue'
import { listAllCategoriesApi } from '@/api/category'
import { listAllTagsApi } from '@/api/tag'

export const useCodeStore = defineStore('code', () => {
  const categories = ref([])
  const tags = ref([])

  /**
   * 加载所有分类（用于下拉框）
   */
  async function loadCategories() {
    try {
      const res = await listAllCategoriesApi()
      categories.value = res.data || []
    } catch (e) {
      categories.value = []
    }
  }

  /**
   * 加载所有标签（用于下拉框/筛选）
   */
  async function loadTags() {
    try {
      const res = await listAllTagsApi()
      tags.value = res.data || []
    } catch (e) {
      tags.value = []
    }
  }

  return {
    categories,
    tags,
    loadCategories,
    loadTags
  }
})
