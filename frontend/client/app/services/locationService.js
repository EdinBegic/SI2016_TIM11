import BaseService from './baseService';

export default class LocationService extends BaseService {
	all() {
		return super.get('locations/all');
	}

	create(account) {
		return super.post('locations', account);
	}

	delete(id) {
		return super.delete('locations/' + id);
	}

	getPage(page) {
		return super.get('locations/page/'+page);
	}

    find(id) {
        return super.get('locations/' + id);
    }

    update(id, location) {
        return super.post('locations/' + id, location);
    }

    filterByName(name) {
        return super.get('locations/filter-by/name?name=' + name);
    }
}
