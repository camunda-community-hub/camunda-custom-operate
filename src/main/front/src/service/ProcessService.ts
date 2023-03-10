import api from './api';

export class ProcessService {

  listProcesses = async ():Promise<any[]> => {
    const { data } = await api.get<any[]>('/process/definition/latest/');
    return data;
  }
  listVersions = async (bpmnProcessId: string): Promise<any[]> => {
    const { data } = await api.get<any[]>('/process/definition/' + bpmnProcessId +'/versions');
    return data;
  }
  getDefinition = async (key: number): Promise<string> => {
    const { data } = await api.get<string>('/process/definition/' + key + '/xml');
    return data;
  }
  loadInstances = async (bpmnProcessId: string, version: number): Promise<any[]> => {
    const { data } = await api.get<any[]>('/process/'+bpmnProcessId+'/'+version+'/instances');
    return data;
  }
  loadInstance = async (key: number): Promise<any> => {
    const { data } = await api.get<any>('/process/' + key);
    return data;
  }
  getHistory = async (key: number): Promise<any[]> => {
    const { data } = await api.get<any[]>('/process/' + key + '/flownodes');
    return data;
  }
  getVariables = async (key: number): Promise<any[]> => {
    const { data } = await api.get<any[]>('/process/' + key + '/variables');
    return data;
  }
  submitChangeRequest = async (request: any): Promise<any> => {
    const { data } = await api.post<any>('/instance/modif', request);
    return data;
  }
  loadChangeRequests = async (): Promise<any[]> => {
    const { data } = await api.get<any[]>('/instance/modif');
    return data;
  }
  loadChangeRequest = async (id:number): Promise<any> => {
    const { data } = await api.get<any>('/instance/modif/'+id);
    return data;
  }
  closeRequest = async (id: number, state: string): Promise<any> => {
    const { data } = await api.get<any>('/instance/modif/' + id+'/'+state);
    return data;
  }
}

const processService = new ProcessService();

export default processService;
