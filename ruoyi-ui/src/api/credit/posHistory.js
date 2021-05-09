import request from '@/utils/request'

// 查询pos机交易历史列表
export function listPosHistory(query) {
  return request({
    url: '/credit/posHistory/list',
    method: 'get',
    params: query
  })
}

// 查询pos机交易历史详细
export function getPosHistory(id) {
  return request({
    url: '/credit/posHistory/' + id,
    method: 'get'
  })
}

// 新增pos机交易历史
export function addPosHistory(data) {
  return request({
    url: '/credit/posHistory',
    method: 'post',
    data: data
  })
}

// 修改pos机交易历史
export function updatePosHistory(data) {
  return request({
    url: '/credit/posHistory',
    method: 'put',
    data: data
  })
}

// 删除pos机交易历史
export function delPosHistory(id) {
  return request({
    url: '/credit/posHistory/' + id,
    method: 'delete'
  })
}

// 导出pos机交易历史
export function exportPosHistory(query) {
  return request({
    url: '/credit/posHistory/export',
    method: 'get',
    params: query
  })
}

// pos机列表
export function getPosList() {
  return request({
    url: '/credit/pos/posList',
    method: 'get'
  })
}
