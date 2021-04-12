import request from '@/utils/request'

// 查询pos机列表
export function listPos(query) {
  return request({
    url: '/credit/pos/list',
    method: 'get',
    params: query
  })
}

// 查询pos机详细
export function getPos(id) {
  return request({
    url: '/credit/pos/' + id,
    method: 'get'
  })
}

// 新增pos机
export function addPos(data) {
  return request({
    url: '/credit/pos',
    method: 'post',
    data: data
  })
}

// 修改pos机
export function updatePos(data) {
  return request({
    url: '/credit/pos',
    method: 'put',
    data: data
  })
}

// 删除pos机
export function delPos(id) {
  return request({
    url: '/credit/pos/' + id,
    method: 'delete'
  })
}

// 导出pos机
export function exportPos(query) {
  return request({
    url: '/credit/pos/export',
    method: 'get',
    params: query
  })
}
