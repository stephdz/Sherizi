package fr.dz.sherizi.gui;

import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.service.share.ShareManager;
import fr.dz.sherizi.service.share.TransferInformations;
import fr.dz.sherizi.utils.Utils;

/**
 * Activity for incoming transfer
 */
public class IncomingTransferActivity extends SheriziActivity {

	// TODO To be completed

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets the transfer informations about the current transfer
        Integer transferId = getIntent().getIntExtra(Utils.EXTRA_ID, -1);
        if ( transferId != -1 ) {
	        final TransferInformations transferInformations = getSheriziApplication().getWaitingTransfer(transferId);

	        // Shows a confirm dialog
	        new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.app_name)
		        .setMessage(R.string.accept_transfer) // TODO Display more informations
		        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	doReceive(IncomingTransferActivity.this, transferInformations);
		            }
		        })
		        .setNegativeButton(R.string.no, null)
		        .show();
        }
    }

	/**
	 * Does the receive job
	 * @param context
	 * @param transferInformations
	 */
	protected void doReceive(Context context, TransferInformations transferInformations) {

		// Gets the share manager
		final ShareManager shareManager = ShareManager.getShareManager(transferInformations.getShareManagerId(), context);

		// Receive datas
		shareManager.receive(transferInformations, new SheriziActionListener() {
			@Override
			public void onSuccess() {
				try {
					OutputStream outputStream = Utils.openFileOutput("test.jpg");
					outputStream.write(shareManager.getData().getFileContent());
					outputStream.close();
				} catch(Throwable t) {
					onError(new SheriziException("Error while saving received file", t));
				}
			}

			@Override
			public void onError(Throwable t) {
				// TODO Log errors
			}
		});
	}
}
