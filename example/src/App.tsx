import React, { useEffect, useState } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import {
  getPermissionReadPhoneNumber,
  getPermissionReadSms,
} from './permission';
import { getOwnPhoneNumber } from 'react-native-own-phone-number';

export default function App() {
  const [phoneNumber, setPhoneNumber] = useState<string>('');

  useEffect(() => {
    _checkOwnPhoneNumber();
  }, []);

  async function _checkOwnPhoneNumber() {
    await getPermissionReadPhoneNumber().catch((error) => {
      console.log(error);
      return;
    });
    await getPermissionReadSms().catch((error) => {
      console.log(error);
      return;
    });
    const myPhone = await getOwnPhoneNumber();
    console.log('myPhone => ', myPhone);
    setPhoneNumber(myPhone);
  }

  return (
    <View style={styles.container}>
      <Text>Result: {phoneNumber}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
