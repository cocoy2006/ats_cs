package molab.main.java.util;

import molab.main.java.entity.Script;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorModel;
import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.RootWindowNode;
import com.android.uiautomator.tree.UiNode;

public class UiAutomatorUtil {

	public static UiNode findNode(IDevice iDevice, Script script) {
		synchronized(UiAutomatorUtil.class) {
			UiAutomatorModel model = null;
			try {
				model = UiAutomatorHelper.takeModel(iDevice, false);
				if(model != null) {
					RootWindowNode root = (RootWindowNode) model.getXmlRootNode();
					String mPath = script.getMpath(); 
					UiNode foundNode = findNode(root, mPath);
					if(foundNode != null) {
						if(script.getMid() != null && 
								!script.getMid().equalsIgnoreCase(foundNode.getAttribute("resource-id"))) {
							return model.searchNode(script.getMid());
						} else {
							return foundNode;
						}
					}
				}
			} catch (UiAutomatorException e) {}
			return null;
		}
	}
	
	private static UiNode findNode(RootWindowNode root, String mPath) {
		if(mPath != null) {
			String[] indexList = mPath.split(",");
			try {
				BasicTreeNode node = root.getChildren()[Integer.parseInt(indexList[1])];
				for(int i = 2; i < indexList.length; i++) {
					int index = Integer.parseInt(indexList[i]);
					node = node.getChildren()[index];
				}
				if(node != null) {
					return (UiNode) node;
				}
			} catch(Exception e) {}
		}
		return null;
	}
	
}
