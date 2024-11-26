package com.example.demo.Service;

import com.example.demo.Model.Route;
import com.example.demo.Repository.RouteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteSV {
    @Autowired
    private RouteRepo routeRepo;

    // Phương thức để lấy danh sách các tuyến đường duy nhất
    public List<String> getUniqueFromLocations() {
        return routeRepo.findUniqueFromLocations();
    }
    public List<String> findUniqueToLocations() {
        return routeRepo.findUniqueToLocations();
    }
    // Cập nhật phương thức getBusSchedules để chấp nhận các tham số bổ sung
    public List<Object[]> getBusSchedules(String pickup, String dropoff, LocalDate departureDate,
                                          Double lowestPrice, Double highestPrice,
                                          List<String> busTypes, String sortParam,
                                          LocalDateTime currentDateTimePlusOneHour) { // Thêm tham số mới
        return routeRepo.findSchedulesWithDetails(pickup, dropoff, departureDate,
                currentDateTimePlusOneHour, // Đặt đúng vị trí tham số
                lowestPrice, highestPrice, busTypes, sortParam);
    }

    public Map<String, Object> findAllBusRoutes(int limit, int offset) {
        // Tính toán số trang từ offset và limit
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);

        // Gọi repository để lấy dữ liệu phân trang
        Page<Object[]> pageResult = routeRepo.findAllBusRoutes(pageable);

        // Chuẩn bị phản hồi trả về với dữ liệu và thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("data", pageResult.getContent()); // Dữ liệu của trang hiện tại
        response.put("totalElements", pageResult.getTotalElements()); // Tổng số bản ghi
        response.put("currentPage", pageResult.getNumber()); // Số trang hiện tại
        response.put("totalPages", pageResult.getTotalPages()); // Tổng số trang

        return response;
    }



    public List<Object[]> getMostPopularRoute(int numLimit) {
        Pageable pageable = PageRequest.of(0, numLimit);
        List<Object[]> results = routeRepo.findMostPopularRoute(pageable);
        return results.isEmpty() ? null : results; // Return the top result or null if not found
    }

    public List<Route> getRouteLimit(int page, int size) {
        return routeRepo.getRouteLimit(PageRequest.of(page, size));
    }
    public Integer getTotalRoute() {
        return routeRepo.findAll().size();
    }

}
