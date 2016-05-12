package com.njdp.njdp_drivers.items;

import com.njdp.njdp_drivers.R;

import java.util.HashMap;

public class SimpleGuideModle {
	private static SimpleGuideModle mInstance = null;
	
	HashMap<String, Integer> mTurnIconMap = new HashMap<String, Integer>();
	HashMap<String, String> mTurnNameMap = new HashMap<String, String>();
	
	public SimpleGuideModle() {
		int size = gTurnIconName.length;
		for (int i = 0; i < size; i++) {
			mTurnIconMap.put(gTurnIconName[i], gTurnIconID[i]);
		}
	};

	public static SimpleGuideModle getInstance() {
		if (mInstance == null) {
			mInstance = new SimpleGuideModle();
		}
		return mInstance;
	}
	
	public int getTurnIconResId(String turnName) {
		if (mTurnIconMap.containsKey(turnName)) {
			return mTurnIconMap.get(turnName);
		}
		
		return gTurnIconID[0];
	}

	public static final String[] gTurnIconName = {
    	"turn_back.png" ,			      /**<  无效值 */
    	"turn_front.png" ,			      /**<  直行 */
    	"turn_right_front.png" ,		  /**<  右前方转弯 */
    	"turn_right.png" ,			      /**<  右转 */
    	"turn_right_back.png" ,		      /**<  右后方转弯 */
    	"turn_back.png" ,				  /**<  掉头 */
    	"turn_left_back.png" ,		      /**<  左后方转弯 */
    	"turn_left.png" ,				  /**<  左转 */
    	"turn_left_front.png" ,		      /**<  左前方转弯 */
    	"turn_ring.png" ,				  /**<  环岛 */
    	"turn_ring_out.png" ,			  /**<  环岛出口 */
    	"turn_left_side.png" ,		      /**<  普通/JCT/SAPA二分歧 靠左 */
    	"turn_right_side.png" ,		      /**<  普通/JCT/SAPA二分歧 靠右 */
    	"turn_left_side_main.png" ,	      /**<  左侧走本线 */
        "turn_branch_left_straight.png" , /**<  靠最左走本线 */
    	"turn_right_side_main.png" ,	  /**<  右侧走本线 */
        "turn_branch_right_straight.png", /**<  靠最右走本线 */
        "turn_branch_center.png" ,        /**<  中间走本线 */
    	"turn_left_side_ic.png" ,		  /**<  IC二分歧左侧走IC */
    	"turn_right_side_ic.png" ,	      /**<  IC二分歧右侧走IC */
    	"turn_branch_left.png" ,		  /**<  普通三分歧/JCT/SAPA 靠最左 */
    	"turn_branch_right.png" ,		  /**<  普通三分歧/JCT/SAPA 靠最右 */
    	"turn_branch_center.png" ,	      /**<  普通三分歧/JCT/SAPA 靠中间 */
    	"turn_start.png" ,			      /**<  起始地 */
    	"turn_dest.png" ,			      /**<  目的地 */
    	"turn_via_1.png" ,			      /**<  途经点1 */
    	"turn_via_2.png" ,			      /**<  途经点2 */
    	"turn_via_3.png" ,			      /**<  途经点3 */
    	"turn_via_4.png" ,		          /**<  途经点4 */
    	"turn_inferry.png" ,			  /**<  进入渡口 */
    	"turn_outferry.png" ,			  /**<  脱出渡口 */
        "turn_tollgate.png" ,             /**<  收费站 */
        "turn_left_side_main.png" ,       /**<  IC二分歧左侧直行走IC */
    	"turn_right_side_main.png" ,      /**<  IC二分歧右侧直行走IC */
    	"turn_left_side_main.png" ,       /**<  普通/JCT/SAPA二分歧左侧 直行 */
    	"turn_right_side_main.png" ,      /**<  普通/JCT/SAPA二分歧右侧 直行 */
    	"turn_branch_left_straight.png" , /**<  普通/JCT/SAPA三分歧左侧 直行 */
    	"turn_branch_center.png" ,        /**<  普通/JCT/SAPA三分歧中央 直行 */
    	"turn_branch_right_straight.png" ,/**<  普通/JCT/SAPA三分歧右侧 直行 */
        "turn_branch_left.png" ,          /**<  IC三分歧左侧走IC */
    	"turn_branch_center.png" ,        /**<  IC三分歧中央走IC */
    	"turn_branch_right.png" ,         /**<  IC三分歧右侧走IC */
        "turn_branch_left_straight.png" , /**<  IC三分歧左侧直行 */
    	"turn_branch_center.png" ,	      /**<  IC三分歧中间直行 */
    	"turn_branch_right_straight.png",  /**<  IC三分歧右侧直行 */
    	"turn_left_side_main.png",  /**<  八方向靠左直行*/
    	"turn_right_side_main.png",  /**<  八方向靠右直行*/
    	"turn_branch_left_straight.png",  /**<  八方向靠最左侧直行*/
    	"turn_branch_center.png",  /**<  八方向沿中间直行*/
    	"turn_branch_right_straight.png",  /**<  八方向靠最右侧直行*/
    	"turn_left_2branch_left.png",  /**<  八方向左转+随后靠左*/
    	"turn_left_2branch_right.png",  /**<  八方向左转+随后靠右*/
    	"turn_left_3branch_left.png",  /**<  八方向左转+随后靠最左*/
    	"turn_left_3branch_middle.png",  /**<  八方向左转+随后沿中间*/
    	"turn_left_3branch_right.png",  /**<  八方向左转+随后靠最右*/
    	"turn_right_2branch_left.png",  /**<  八方向右转+随后靠左 */
    	"turn_right_2branch_right.png",  /**<  八方向右转+随后靠右*/
    	"turn_right_3branch_left.png",  /**<  八方向右转+随后靠最左*/
    	"turn_right_3branch_middle.png",  /**<  八方向右转+随后沿中间 */
    	"turn_right_3branch_right.png",  /**<  八方向右转+随后靠最右*/    	
    	
    	"turn_lf_2branch_left.png",    /**<  八方向左前方靠左侧 */
        "turn_lf_2branch_right.png",    /**<  八方向左前方靠右侧 */  
        "turn_rf_2branch_left.png",     /**<  八方向右前方靠左侧 */
        "turn_rf_2branch_right.png",     /**<  八方向右前方靠右侧 */
        
        "turn_back_2branch_left_base.png" ,      /**<  八方向掉头+随后靠左 */
        "turn_back_2branch_right_base.png" ,     /**<  八方向掉头+随后靠右 */
        "turn_back_3branch_left_base.png" ,    /**<  八方向掉头+随后靠最左 */
        "turn_back_3branch_middle_base.png" ,    /**<  八方向掉头+随后沿中间 */
        "turn_back_3branch_right_base.png" ,     /**<  八方向掉头+随后靠最右 */
        
        "turn_ring_front.png",      /**<  环岛向前 */
        "turn_ring_left.png",     /**<  环岛向左 */
        "turn_ring_leftback.png",     /**<  环岛向左后 */
        "turn_ring_leftfront.png",     /**<  环岛向左前 */
        
        "turn_ring_right.png",     /**<  环岛向右 */
        "turn_ring_rightback.png",     /**<  环岛向右后 */
        "turn_ring_rightfront.png",     /**<  环岛向右前 */
        "turn_ring_back.png"     /**<  环岛向后 */
    };
	
	public static final int[] gTurnIconID = {
			R.drawable.nsdk_drawable_rg_ic_turn_back  ,			      /**<  无效值 */
    	R.drawable.nsdk_drawable_rg_ic_turn_front  ,			      /**<  直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_front  ,		  /**<  右前方转弯 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right  ,			      /**<  右转 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_back  ,		      /**<  右后方转弯 */
    	R.drawable.nsdk_drawable_rg_ic_turn_back  ,				  /**<  掉头 */
    	R.drawable.nsdk_drawable_rg_ic_turn_left_back  ,		      /**<  左后方转弯 */
    	R.drawable.nsdk_drawable_rg_ic_turn_left  ,				  /**<  左转 */
    	R.drawable.nsdk_drawable_rg_ic_turn_left_front  ,		      /**<  左前方转弯 */
    	R.drawable.nsdk_drawable_rg_ic_turn_ring  ,				  /**<  环岛 */
    	R.drawable.nsdk_drawable_rg_ic_turn_ring_out  ,			  /**<  环岛出口 */
    	R.drawable.nsdk_drawable_rg_ic_turn_left_side  ,		      /**<  普通/JCT/SAPA二分歧 靠左 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_side  ,		      /**<  普通/JCT/SAPA二分歧 靠右 */
    	R.drawable.nsdk_drawable_rg_ic_turn_left_side_main  ,	      /**<  左侧走本线 */
        R.drawable.nsdk_drawable_rg_ic_turn_branch_left_straight  , /**<  靠最左走本线 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_side_main  ,	  /**<  右侧走本线 */
        R.drawable.nsdk_drawable_rg_ic_turn_branch_right_straight , /**<  靠最右走本线 */
        R.drawable.nsdk_drawable_rg_ic_turn_branch_center  ,        /**<  中间走本线 */
    	R.drawable.nsdk_drawable_rg_ic_turn_left_side_ic  ,		  /**<  IC二分歧左侧走IC */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_side_ic  ,	      /**<  IC二分歧右侧走IC */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_left  ,		  /**<  普通三分歧/JCT/SAPA 靠最左 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_right  ,		  /**<  普通三分歧/JCT/SAPA 靠最右 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_center  ,	      /**<  普通三分歧/JCT/SAPA 靠中间 */
    	R.drawable.nsdk_drawable_rg_ic_turn_start  ,			      /**<  起始地 */
    	R.drawable.nsdk_drawable_rg_ic_turn_dest  ,			      /**<  目的地 */
    	R.drawable.nsdk_drawable_rg_ic_turn_via_1,			      /**<  途径点1 */
    	R.drawable.nsdk_drawable_rg_ic_turn_via_1,			      /**<  途径点2 */
    	R.drawable.nsdk_drawable_rg_ic_turn_via_1,			      /**<  途径点3 */
    	R.drawable.nsdk_drawable_rg_ic_turn_via_1,		          /**<  途径点4 */
    	R.drawable.nsdk_drawable_rg_ic_turn_inferry  ,			  /**<  进入渡口 */
    	R.drawable.nsdk_drawable_rg_ic_turn_inferry ,			  /**<  脱出渡口 */
        R.drawable.nsdk_drawable_rg_ic_turn_tollgate  ,             /**<  收费站 */
        R.drawable.nsdk_drawable_rg_ic_turn_left_side_main  ,       /**<  IC二分歧左侧直行走IC */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_side_main  ,      /**<  IC二分歧右侧直行走IC */
    	R.drawable.nsdk_drawable_rg_ic_turn_left_side_main  ,       /**<  普通/JCT/SAPA二分歧左侧 直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right_side_main  ,      /**<  普通/JCT/SAPA二分歧右侧 直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_left_straight  , /**<  普通/JCT/SAPA三分歧左侧 直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_center  ,        /**<  普通/JCT/SAPA三分歧中央 直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_right_straight  ,/**<  普通/JCT/SAPA三分歧右侧 直行 */
        R.drawable.nsdk_drawable_rg_ic_turn_branch_left  ,          /**<  IC三分歧左侧走IC */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_center  ,        /**<  IC三分歧中央走IC */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_right  ,         /**<  IC三分歧右侧走IC */
        R.drawable.nsdk_drawable_rg_ic_turn_branch_left_straight  , /**<  IC三分歧左侧直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_center  ,	      /**<  IC三分歧中间直行 */
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_right_straight,   /**<  IC三分歧右侧直行 */
      	R.drawable.nsdk_drawable_rg_ic_turn_left_side_main,  /**<  八方向靠左直行*/
    	R.drawable.nsdk_drawable_rg_ic_turn_right_side_main,  /**<  八方向靠右直行*/
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_left_straight, /**<  八方向靠最左侧直行*/
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_center,  /**<  八方向沿中间直行*/
    	R.drawable.nsdk_drawable_rg_ic_turn_branch_right_straight,  /**<  八方向靠最右侧直行*/
    	R.drawable.nsdk_drawable_rg_ic_turn_left,  /**<  八方向左转+随后靠左*/
    	R.drawable.nsdk_drawable_rg_ic_turn_left,  /**<  八方向左转+随后靠右*/
    	R.drawable.nsdk_drawable_rg_ic_turn_left,  /**<  八方向左转+随后靠最左*/
    	R.drawable.nsdk_drawable_rg_ic_turn_left,/**<  八方向左转+随后沿中间*/
    	R.drawable.nsdk_drawable_rg_ic_turn_left,  /**<  八方向左转+随后靠最右*/
    	R.drawable.nsdk_drawable_rg_ic_turn_right, /**<  八方向右转+随后靠左 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right,  /**<  八方向右转+随后靠右*/
    	R.drawable.nsdk_drawable_rg_ic_turn_right, /**<  八方向右转+随后靠最左*/
    	R.drawable.nsdk_drawable_rg_ic_turn_right, /**<  八方向右转+随后沿中间 */
    	R.drawable.nsdk_drawable_rg_ic_turn_right,  /**<  八方向右转+随后靠最右*/
    	
    	R.drawable.nsdk_drawable_rg_ic_turn_lf_2branch_left ,    /**<  八方向左前方靠左侧 */
        R.drawable.nsdk_drawable_rg_ic_turn_lf_2branch_right ,    /**<  八方向左前方靠右侧 */  
        R.drawable.nsdk_drawable_rg_ic_turn_rf_2branch_left ,     /**<  八方向右前方靠左侧 */
        R.drawable.nsdk_drawable_rg_ic_turn_rf_2branch_right ,    /**<  八方向右前方靠右侧 */
        
        R.drawable.nsdk_drawable_rg_ic_turn_back  ,       /**<  八方向掉头+随后靠左 */
        R.drawable.nsdk_drawable_rg_ic_turn_back  ,     /**<  八方向掉头+随后靠右 */
        R.drawable.nsdk_drawable_rg_ic_turn_back  ,     /**<  八方向掉头+随后靠最左 */
        R.drawable.nsdk_drawable_rg_ic_turn_back  ,     /**<  八方向掉头+随后沿中间 */
        R.drawable.nsdk_drawable_rg_ic_turn_back  ,      /**<  八方向掉头+随后靠最右 */
        
        R.drawable.nsdk_drawable_rg_ic_turn_ring_front,    /**<  环岛向前 */
        R.drawable.nsdk_drawable_rg_ic_turn_ring_left,    /**<  环岛向左 */
        R.drawable.nsdk_drawable_rg_ic_turn_ring_leftback,    /**<  环岛向左后 */
        R.drawable.nsdk_drawable_rg_ic_turn_ring_leftfront,    /**<  环岛向左前 */
        
        R.drawable.nsdk_drawable_rg_ic_turn_ring_right,    /**<  环岛向右 */
        R.drawable.nsdk_drawable_rg_ic_turn_ring_rightback,    /**<  环岛向右后 */
        R.drawable.nsdk_drawable_rg_ic_turn_ring_rightfront,    /**<  环岛向右前 */
        R.drawable.nsdk_drawable_rg_ic_turn_ring_turnback    /**<  环岛向后 */
    };
}
