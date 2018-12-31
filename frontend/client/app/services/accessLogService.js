import BaseService from './baseService';

export default class AccessLogService extends BaseService {
	all() {
		return super.get('access-logs/all'); 
	}

    getPage(page) {
		return super.get('access-logs/page/' + page);
	}
	
    getByFilter(searchedText) {
        return super.get('access-logs/search-by/filter?filter=' + searchedText);
    }
}
