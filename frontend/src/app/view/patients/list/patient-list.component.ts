declare var Ext: any;
import {Component, HostBinding} from '@angular/core';
import {gridData} from './data';

@Component({
  selector: 'app-patient-list',
  templateUrl: './patient-list.component.html',
  styles: []
})
export class PatientListComponent {
  //@HostBinding('style.background-color') public color: string = 'white';
  @HostBinding('style.height') public height: string = '100%';
  //@HostBinding('class') classes = 'classA classB';
  store = Ext.create('Ext.data.Store', {
    fields: ['id', 'fullname', 'pesel', 'birthday', 'address', 'status', 'version'],
    data: gridData
  });
  gridCmp: any;

  readyGrid = ({cmp, cmpObj}) => {
    this.gridCmp = cmp;
    //this.gridCmp.setStore(this.store);
  }

  onSearch = (event) => {
    const query = event.newValue.toLowerCase();
    this.store.clearFilter();
    if (query.length) this.store.filterBy(record => {
      const {name, email, phone} = record.data;
      return name.toLowerCase().indexOf(query) !== -1 ||
              email.toLowerCase().indexOf(query) !== -1 ||
              phone.toLowerCase().indexOf(query) !== -1;
    });
  }

}
