package io.github.chenyilei2016.nettycluster.domain;

/**
 * 接口反馈信息类
 */
public class EasyResult {

    private boolean success;
    private String title;
    private String msg;

    static public EasyResult buildSuccessResult() {
        EasyResult easyResult = new EasyResult();
        easyResult.setSuccess(true);
        easyResult.setMsg("ok");
        return easyResult;
    }

    static public EasyResult buildErrResult(Exception e) {
        EasyResult easyResult = new EasyResult();
        easyResult.setSuccess(false);
        easyResult.setMsg(e.getMessage());
        return easyResult;
    }

    static public EasyResult buildErrResult(String msg) {
        EasyResult easyResult = new EasyResult();
        easyResult.setSuccess(false);
        easyResult.setMsg(msg);
        return easyResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
