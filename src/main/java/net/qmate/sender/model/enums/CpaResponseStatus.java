package net.qmate.sender.model.enums;

public enum CpaResponseStatus {
    OK(200), ACCEPTED(202), BAD_REQUEST(400), UNAUTHORIZED(401), FORBIDDEN(403),
    NOT_FOUND(404), INTERNAL_ERROR(500), BAD_GATEWAY(502);
    public int code;

    CpaResponseStatus(int code) {
        this.code = code;
    }

}
