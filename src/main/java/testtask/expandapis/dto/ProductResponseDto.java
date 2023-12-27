package testtask.expandapis.dto;

public record ProductResponseDto(
        Long id, 
        String entryDate, 
        String itemCode, 
        String itemName,
        Long itemQuantity, 
        String status) {
}
