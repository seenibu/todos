package sn.ept.git.seminaire.cicd.models;

import lombok.Builder;
import java.time.LocalDateTime;

/**
 *
 * @author ISENE
 */

@Builder
public record ErrorModel (String systemId,String systemName,String type, int status, LocalDateTime date, String message, String description) {}
