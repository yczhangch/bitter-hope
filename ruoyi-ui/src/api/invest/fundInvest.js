import request from '@/utils/request'

// 查询基金投资列表
export function listFundInvest(query) {
  return request({
    url: '/invest/fundInvest/list',
    method: 'get',
    params: query
  })
}

// 查询基金投资详细
export function getFundInvest(id) {
  return request({
    url: '/invest/fundInvest/' + id,
    method: 'get'
  })
}

// 新增基金投资
export function addFundInvest(data) {
  return request({
    url: '/invest/fundInvest',
    method: 'post',
    data: data
  })
}

// 修改基金投资
export function updateFundInvest(data) {
  return request({
    url: '/invest/fundInvest',
    method: 'put',
    data: data
  })
}

// 删除基金投资
export function delFundInvest(id) {
  return request({
    url: '/invest/fundInvest/' + id,
    method: 'delete'
  })
}

// 导出基金投资
export function exportFundInvest(query) {
  return request({
    url: '/invest/fundInvest/export',
    method: 'get',
    params: query
  })
}