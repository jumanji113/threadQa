package models.swagger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorAuth{
	private String path;
	private String error;
	private String timestamp;
	private int status;
}
