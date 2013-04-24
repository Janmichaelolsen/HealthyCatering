package Mobile;
/*1
import java.util.Map;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import org.primefaces.mobile.renderkit.MobileRenderKit;

public class MobileViewHandler extends ViewHandlerWrapper {
    private ViewHandler wrapped;

    public MobileViewHandler(ViewHandler wrapped) {
        this.wrapped = wrapped;
    }
    @Override
    public ViewHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public String calculateRenderKitId(FacesContext context) {
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();

        if (map.containsKey("mobile")) {
            return MobileRenderKit.RENDER_KIT_ID;
        } else {
            return this.wrapped.calculateRenderKitId(context);
        }
    }
}
*/