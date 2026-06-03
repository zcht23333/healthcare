import request from '@/utils/request'

export function getLocationList(params) { return request({ url: '/location/list', method: 'get', params }) }
export function addLocation(data) { return request({ url: '/location/add', method: 'post', data }) }
export function updateLocation(data) { return request({ url: '/location/update', method: 'post', data }) }
export function deleteLocation(id) { return request({ url: `/location/delete/${id}`, method: 'post' }) }