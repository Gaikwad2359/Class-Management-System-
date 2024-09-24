package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.LaptopAllocationDAO;
import CMS.Master.LaptopAllocationMaster;
import CMS.Master.StudentMaster;
import CMS.Service.LaptopAllocationService;

@Service
public class LaptopAllocationServiceImpl implements LaptopAllocationService {

    @Autowired
    LaptopAllocationDAO dao;

    @Override
    public List<LaptopAllocationMaster> getAllLaptopAllocations() {
        return dao.findAll();
    }

    @Override
    public Page<LaptopAllocationMaster> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return dao.findAll(pageable);
    }

    @Override
    public LaptopAllocationMaster addLaptopAllocation(LaptopAllocationMaster laptopAllocation) {
        return dao.save(laptopAllocation);
    }

    @Override
    public LaptopAllocationMaster updateLaptopAllocation(LaptopAllocationMaster laptopAllocation, int id) {
        return dao.save(laptopAllocation);
    }

    @Override
    public void deleteLaptopAllocation(int id) {
        dao.deleteById(id);
    }

    @Override
    public LaptopAllocationMaster findLaptopAllocationById(int id) {
        Optional<LaptopAllocationMaster> laptopAllocation = dao.findById(id);
        return laptopAllocation.orElse(null);
    }

    @Override
    public Object getAllLaptops() {
        // Implementation for retrieving all laptops
        return null;
    }
    
    @Override
    public Object getAllStudents() {
        // Implementation for retrieving all laptops
        return null;
    }

    @Override
    public void updateLaptopAllocation(LaptopAllocationMaster laptopAllocation) {
        LaptopAllocationMaster existingLaptopAllocation = dao.findById(laptopAllocation.getId())
            .orElseThrow(() -> new RuntimeException("Laptop Allocation not found with id " + laptopAllocation.getId()));
        existingLaptopAllocation.setLaptopMaster(laptopAllocation.getLaptopMaster());
        existingLaptopAllocation.setStudentMaster(laptopAllocation.getStudentMaster());
        existingLaptopAllocation.setAllocationDate(laptopAllocation.getAllocationDate());
        existingLaptopAllocation.setLaptopCost(laptopAllocation.getLaptopCost());
        existingLaptopAllocation.setRemarks(laptopAllocation.getRemarks());
        dao.save(existingLaptopAllocation);
    }
    
    //Student Laptop
    public LaptopAllocationServiceImpl(LaptopAllocationDAO laptopAllocationRepository) {
        this.dao = laptopAllocationRepository;
    }

    @Override
    public List<LaptopAllocationMaster> getLaptopAllocationsByStudent(StudentMaster student) {
        return dao.findByStudentMaster(student);
    }
}
