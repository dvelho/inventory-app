package cloud.minka.service.model.tenant;

public record TenantStylePersonalization(
        String logoUrl,
        String primaryColor,
        String backgroundColor,
        String textColor,
        String linkColor,
        String linkHoverColor,
        String buttonColor,
        String buttonHoverColor
) {
}
