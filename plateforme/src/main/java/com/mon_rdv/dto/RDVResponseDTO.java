// src/main/java/com/mon_rdv/dto/RDVResponseDTO.java

package com.mon_rdv.dto;





public class RDVResponseDTO {
    private Long id;
    private Long availabilityId;
    private String startTime;
    private String endTime;
    private String status;
    

    public RDVResponseDTO(Long id, Long availabilityId, String startTime, String endTime ,String status) {
        this.id = id;
        this.availabilityId = availabilityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getAvailabilityId() { 
        return availabilityId; 
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    
    public String getStatus() { 
        return status; 
    }
}
