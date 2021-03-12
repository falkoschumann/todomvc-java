import React from 'react';

import { MessageHandling } from '../contract/MessageHandling';

type MessageHandlingContextType = Readonly<{
  messageHandling: MessageHandling;
}>;

const MessageHandlingContext = React.createContext<MessageHandlingContextType>(undefined as any);

type MessageHandlingProviderProps = Readonly<{
  messageHandling: MessageHandling;
  children?: JSX.Element | JSX.Element[];
}>;

export default function MessageHandlingProvider({ messageHandling, children }: MessageHandlingProviderProps) {
  return <MessageHandlingContext.Provider value={{ messageHandling }}>{children}</MessageHandlingContext.Provider>;
}

export function useMessageHandling(): MessageHandling {
  return React.useContext(MessageHandlingContext).messageHandling;
}
