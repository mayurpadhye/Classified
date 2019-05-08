package mimosale.com.helperClass;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Karshima on 8/10/2017.
 */

public class CustomPermissions {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 121;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 122;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    public static final int MY_PERMISSIONS_REQUEST_FILE = 124;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 125;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA_VIDEO = 126;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_VIDEO = 127;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION_1=128;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION_99=129;
    public static final int GOOGLE_PLACE_SEARCH=777;

    public static boolean checkLocationPermission(Activity callingActivity) {
        if ((ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) &&
        (ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkCoarseLocationPermission(Activity callingActivity) {
        if ((ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkCoarseLocationPermission2(Activity callingActivity) {
        if ((ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION_1);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION_1);
            }
            return false;
        } else {
            return true;
        }
    }


    public static boolean checkLocationPermission11(Activity callingActivity) {
        if ((ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) &&
                (ContextCompat.checkSelfPermission(callingActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION_99);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,},
                        MY_PERMISSIONS_REQUEST_LOCATION_99);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkCameraPermission(Activity callingActivity) {
        if (ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                ||
                (ContextCompat.checkSelfPermission(callingActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                ||
                (ContextCompat.checkSelfPermission(callingActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.CAMERA)
                    &&
                    ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&
                    ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CAMERA);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
            return false;
        } else {
            return true;
        }
    }


    public static boolean checkCallPermission(Activity callingActivity)
    {
        if (ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
            return false;
        } else {
            return true;
        }
    }


    public static boolean checkPermissionForFileAccess(Activity callingActivity)
    {
        if ((ContextCompat.checkSelfPermission(callingActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
                ||
                (ContextCompat.checkSelfPermission(callingActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED))  {

            // Asking user if explanation is needed
            if ((ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
                    &&
                    ActivityCompat.shouldShowRequestPermissionRationale(callingActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(callingActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }



}
