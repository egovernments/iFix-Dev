
CREATE INDEX idx_bill_detail_account_no
  ON pspcl_bill_detail (account_no);

CREATE INDEX idx_bill_detail_bill_no
  ON pspcl_bill_detail (bill_no);

CREATE INDEX idx_bill_detail_orderbycolumn
  ON pspcl_bill_detail (orderbycolumn);

CREATE INDEX idx_bill_detail_bill_issue_date
  ON pspcl_bill_detail (bill_issue_date);

CREATE INDEX idx_bill_detail_date_reading_curr
  ON pspcl_bill_detail (date_reading_curr);

CREATE INDEX idx_bill_detail_date_reading_prev
  ON pspcl_bill_detail (date_reading_prev);


CREATE INDEX idx_payment_detail_acno
  ON pspcl_payment_detail (acno);

CREATE INDEX idx_payment_detail_amt
  ON pspcl_payment_detail (amt);

CREATE INDEX idx_payment_detail_bilissdt
  ON pspcl_payment_detail (bilissdt);

CREATE INDEX idx_payment_detail_txndate
  ON pspcl_payment_detail (txndate);

CREATE INDEX idx_payment_detail_txnid
  ON pspcl_payment_detail (txnid);


CREATE INDEX idx_event_posting_detail_event_type
  ON pspcl_event_posting_detail (event_type);

CREATE INDEX idx_event_posting_detail_status
  ON pspcl_event_posting_detail (status);

CREATE INDEX idx_event_posting_detail_reference_id
  ON pspcl_event_posting_detail (reference_id);
