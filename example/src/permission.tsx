import { Platform, Alert } from 'react-native';
import {
  PERMISSIONS,
  openSettings,
  check,
  request,
  RESULTS,
  type PermissionStatus,
} from 'react-native-permissions';

const MSG_PERMISSION_BLOCKED =
  'Permission is blocked, enable manually on setting';
const MSG_PERMISSION_UNAVAILABLE = 'Permission Unavailable';

export function getPermissionReadPhoneNumber(): Promise<boolean> {
  // NOTE: Android Only
  return new Promise(function (resolve, reject) {
    if (Platform.OS !== 'android') {
      resolve(true);
      return;
    }
    check(PERMISSIONS.ANDROID.READ_PHONE_NUMBERS)
      .then((status: PermissionStatus) => {
        switch (status) {
          case RESULTS.UNAVAILABLE:
            _showAlertUnavailable('Read Phone Number');
            break;

          case RESULTS.BLOCKED:
            _showAlertBlocked('Read Phone Number');
            break;

          case RESULTS.DENIED:
            request(PERMISSIONS.ANDROID.READ_PHONE_NUMBERS)
              .then(() => resolve(true))
              .catch((error) => reject(error));
            break;

          case RESULTS.GRANTED:
          case RESULTS.LIMITED:
            resolve(true);
            break;

          default:
            break;
        }
      })
      .catch((error) => reject(error));
  });
}

export function getPermissionReadSms(): Promise<boolean> {
  // NOTE: Android Only
  return new Promise(function (resolve, reject) {
    if (Platform.OS !== 'android') {
      resolve(true);
      return;
    }
    check(PERMISSIONS.ANDROID.READ_SMS)
      .then((status: PermissionStatus) => {
        switch (status) {
          case RESULTS.UNAVAILABLE:
            _showAlertUnavailable('Read SMS');
            break;

          case RESULTS.BLOCKED:
            _showAlertBlocked('Read SMS');
            break;

          case RESULTS.DENIED:
            request(PERMISSIONS.ANDROID.READ_SMS)
              .then(() => resolve(true))
              .catch((error) => reject(error));
            break;

          case RESULTS.GRANTED:
          case RESULTS.LIMITED:
            resolve(true);
            break;

          default:
            break;
        }
      })
      .catch((error) => reject(error));
  });
}

function _showAlertBlocked(message: string = '') {
  Alert.alert(
    'Information',
    `${message} ${MSG_PERMISSION_BLOCKED}`,
    [
      {
        text: 'Cancel',
        onPress: () => {},
      },
      {
        text: 'Setting',
        onPress: () => openSettings(),
      },
    ],
    { cancelable: true }
  );
}

function _showAlertUnavailable(message: string = '') {
  Alert.alert(
    'Information',
    `${message} ${MSG_PERMISSION_UNAVAILABLE}`,
    [
      {
        text: 'Ok',
        onPress: () => {},
      },
    ],
    { cancelable: true }
  );
}
