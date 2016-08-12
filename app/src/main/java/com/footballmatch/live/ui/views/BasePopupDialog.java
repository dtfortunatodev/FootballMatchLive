package com.footballmatch.live.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.footballmatch.live.R;

/**
 * Created by David Fortunato on 06/08/2016
 * All rights reserved GoodBarber
 */
public class BasePopupDialog extends Dialog
{

    // Views
    private TextView tvDescription;
    private TextView tvBtnLeft;
    private TextView tvBtnRight;

    // Listener
    private OnPopupListener onPopupClickListener;

    public BasePopupDialog(Context context)
    {
        super(context);
        initUI();
    }

    public BasePopupDialog(Context context, int themeResId)
    {
        super(context, themeResId);
        initUI();
    }

    protected BasePopupDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
        initUI();
    }

    /**
     * initialize UI
     */
    private void initUI()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_base_layout);

        // Find Views
        tvDescription = (TextView) findViewById(R.id.tvPopupDescription);
        tvBtnLeft = (TextView) findViewById(R.id.tvPopupBaseButtonLeft);
        tvBtnRight = (TextView) findViewById(R.id.tvPopupBaseButtonRight);


        // Setup Listeners
        tvBtnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onPopupClickListener != null)
                {
                    onPopupClickListener.onLeftBtnClick(BasePopupDialog.this);
                }
            }
        });
        tvBtnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onPopupClickListener != null)
                {
                    onPopupClickListener.onRightBtnClick(BasePopupDialog.this);
                }
            }
        });

        // Set Dialog Listener
        setOnCancelListener(new OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                if (onPopupClickListener != null)
                {
                    onPopupClickListener.onCancelled();
                }
            }
        });
        setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                onPopupClickListener = null;
            }
        });
    }



    public void setOnPopupClickListener(OnPopupListener onPopupClickListener)
    {
        this.onPopupClickListener = onPopupClickListener;
    }

    public TextView getTvDescription()
    {
        return tvDescription;
    }

    public TextView getTvBtnLeft()
    {
        return tvBtnLeft;
    }

    public TextView getTvBtnRight()
    {
        return tvBtnRight;
    }


    /**
     * Setup views on Popup
     * @param description
     * @param textBtnLeft
     * @param textBtnRight
     * @param onPopupClickListener
     */
    public void setupPoup(String description, String textBtnLeft, String textBtnRight, OnPopupListener onPopupClickListener)
    {
        getTvBtnLeft().setText(textBtnLeft);
        getTvBtnRight().setText(textBtnRight);
        getTvDescription().setText(description);
        setOnPopupClickListener(onPopupClickListener);
    }

    /**
     * Initialize Popup
     * @param context
     * @param textBtnLeft
     * @param textBtnRight
     * @param onPopupClickListener
     */
    public static void startPopup(Context context, String description, String textBtnLeft, String textBtnRight, OnPopupListener onPopupClickListener)
    {
        BasePopupDialog basePopupDialog = new BasePopupDialog(context);
        basePopupDialog.setupPoup(description, textBtnLeft, textBtnRight, onPopupClickListener);
        basePopupDialog.show();
    }

    public interface OnPopupListener
    {

        void onLeftBtnClick(BasePopupDialog basePopupDialog);

        void onRightBtnClick(BasePopupDialog basePopupDialog);

        void onCancelled();

    }

}
