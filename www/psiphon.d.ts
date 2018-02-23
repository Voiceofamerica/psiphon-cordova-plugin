
export function config (
  configObject: object,
  successCallback: () => void,
  errorCallback: (err: any) => void,
)

export function start (
  successCallback: () => void,
  errorCallback: (err: any) => void,
)

export function pause (
  successCallback: () => void,
  errorCallback: (err: any) => void,
)

export function port (
  successCallback: (ports: [number]) => void,
  errorCallback: (err: any) => void,
)
