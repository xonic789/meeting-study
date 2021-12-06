package study.devmeetingstudy.service.interfaces;

import study.devmeetingstudy.common.exception.global.error.exception.notfound.AddressNotFoundException;
import study.devmeetingstudy.domain.Address;
import study.devmeetingstudy.dto.address.AddressReqDto;

public interface AddressService {

    Address saveAddress(AddressReqDto addressReqDto);

    Address getAddress(Long addressId);
}
