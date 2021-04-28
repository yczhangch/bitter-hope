import request from '@/utils/request'

// 查询比特币投资列表
export function listInvest(query) {
  return request({
    url: '/invest/btc/list',
    method: 'get',
    params: query
  })
}

// 查询比特币投资详细
export function getInvest(id) {
  return request({
    url: '/invest/btc/' + id,
    method: 'get'
  })
}

// 新增比特币投资
export function addInvest(data) {
  return request({
    url: '/invest/btc',
    method: 'post',
    data: data
  })
}

// 修改比特币投资
export function updateInvest(data) {
  return request({
    url: '/invest/btc',
    method: 'put',
    data: data
  })
}

// 删除比特币投资
export function delInvest(id) {
  return request({
    url: '/invest/btc/' + id,
    method: 'delete'
  })
}

// 导出比特币投资
export function exportInvest(query) {
  return request({
    url: '/invest/btc/export',
    method: 'get',
    params: query
  })
}
