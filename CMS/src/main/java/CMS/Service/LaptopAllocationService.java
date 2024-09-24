package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.LaptopAllocationMaster;
import CMS.Master.StudentMaster;

public interface LaptopAllocationService {
    
    List<LaptopAllocationMaster> getAllLaptopAllocations();
    
    Page<LaptopAllocationMaster> findPaginated(int pageNo, int pageSize);
    
    LaptopAllocationMaster addLaptopAllocation(LaptopAllocationMaster laptopAllocation); 
    
    LaptopAllocationMaster updateLaptopAllocation(LaptopAllocationMaster laptopAllocation, int id);
    
    void deleteLaptopAllocation(int id);
    
    LaptopAllocationMaster findLaptopAllocationById(int id);
    
    Object getAllLaptops();
    
    Object getAllStudents();
    
    void updateLaptopAllocation(LaptopAllocationMaster laptopAllocation);

    
    //Student Laptop
    List<LaptopAllocationMaster> getLaptopAllocationsByStudent(StudentMaster student);
}
